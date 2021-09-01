import React, {useState, useRef, useEffect}from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actionsReservations from '../../reservation/actions';
import {Errors} from '..';

const PayPalReserve = ({deposit, menu, companyId, reservationDate, periodType, diners}) => {

    const dispatch = useDispatch();
    const history = useHistory(); 
    const paypal = useRef();                                                                                                                                                                    
    const [backendErrors, setBackendErrors] = useState(null);

    useEffect(() => {
        window.paypal.Buttons({
            createOrder : (data,actions) => {
                return actions.order.create({
                    purchase_units:[
                        {
                            description: "Usted va a realizar una reserva",
                            amount:{
                                value: deposit,
                                currency: "EUR"
                            },
                        },
                    ],
                });
            },
            onApprove : (data, actions) => {
                return actions.order.capture().then(response => {
                    dispatch(actionsReservations.reservation(
                        menu.id,
                        companyId,
                        reservationDate,
                        periodType,
                        diners,
                        response.id,
                        () => history.push('/reservation/reservation-completed'),
                        errors => setBackendErrors(errors)
                    ));
                });
              },
              onError : (error) => {
                console.log(error);
                alert('El pago no fue realizado debido a un error. Vuelva a intentarlo');
            },
        
            onCancel : (data, actions) => {
                alert('Pago no realizado. El usuario canceló el proceso');
            },
        }).render(paypal.current)
    })

      return (
          <div>
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <div ref={paypal}></div>
          </div>
        
    
      );
};

export default PayPalReserve; 
