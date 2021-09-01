import React, {useState}from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';
import ReactDOM from 'react-dom';
import paypal from 'paypal-checkout';

import * as actionsReservations from '../../reservation/actions';
import {Errors} from '..';


const PayPalRefund = ({reserve}) => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [backendErrors, setBackendErrors] = useState(null);

    const paypalConf = {
        currency: 'EUR',
        env:'sandbox',
        client: {
            sandbox: 'AQaLphq3O_9JZ4Jv1gLco034hKivy_yXBNsEBDcUb8Y4JV9et-mfCE54pjwRS6SV7UIfFV0sdxMWVtBM',
            production: '',
        },
        locale: 'es_ES',
        style: {
            label: 'pay',
            size: 'medium',
            shape: 'pill',
            color: 'black'
        }
    };
    const PayPalButton = paypal.Button.driver('react', {React, ReactDOM});

    const payment = (data, actions) => {
        const payment = {
            transactions: [
                {
                    amount: {
                        total: reserve.deposit,
                        currency: paypalConf.currency,
                    },
                    description: 'Reserva realizada en Relivry'
                }
            ],
        };
        return actions.payment.create({payment});
    };

    const onAuthorize = (data, actions) => {
        return actions.payment.execute().then(() => {
            dispatch(actionsReservations.removeReservation(
                reserve.id,
                () => history.push('/reservation/find-company-reserves-completed'),
                errors => setBackendErrors(errors)
            ));
        })
        .catch(error => {
            console.log(error);
            alert('Ha ocurrido un error al procesar el pago con PayPal');
        });
    };

    const onError = (error) => {
        console.log(error);
        alert('El pago no fue realizado debido a un error. Vuelva a intentarlo');
    };

    const onCancel = (data, actions) => {
        alert('Pago no realizado. El usuario canceló el proceso');
    };

    return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>

            <PayPalButton env={paypalConf.env}
                client={paypalConf.client}
                payment={(data, actions) => payment(data, actions)}
                onAuthorize={(data, actions) => onAuthorize(data, actions)}
                onCancel={(data, actions) => onCancel(data, actions)}
                onError={(error) => onError(error)}
                style={paypalConf.style}
                locale={paypalConf.locale}
                commit/>
        </div>
        
    );
};

export default PayPalRefund; 
