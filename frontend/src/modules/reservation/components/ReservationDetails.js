import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {useSelector} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import MenuItemList from './MenuItemList';
import * as selectors from '../selectors';
import moment from 'moment';
import 'moment/locale/es';
import PayPalReserve from '../../common/components/PayPalReserve';

const ReservationDetails = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const menu = useSelector(selectors.getMenu);
    const [backendErrors, setBackendErrors] = useState(null);
    const {id, reservationDate, periodType, diners} = useParams();
    const companyId = id;
    var dateEs = moment(new Date(reservationDate)).format('LL');
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.reservation(
                menu.id,
                companyId,
                reservationDate,
                periodType,
                diners,
                () => history.push('/reservation/reservation-completed'),
                errors => setBackendErrors(errors)
            ));

        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }

    }

    if (menu.items.length === 0) {
        return null;
    }
    
   return (
        <div>
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <div className="container-fluid">
                <div className="card col-lg-5 mt-3 mr-2 ml-2">
                    <div className="card-body">
                        { periodType === 'LUNCH' ?
                            <h4 className="card-title">Usted ha realizado un reserva para el día {dateEs} para comer</h4>
                        :
                            <h4 className="card-title">Usted ha realizado un reserva para el día {dateEs} para cenar</h4>
                        }
                        <h6 class="mt-4">La mesa reservada será para {diners} comensal/es</h6>
                    </div>
                </div>
            </div>
            <MenuItemList list={menu}/>
            {menu.deposit > Number(0) ? 
                <div>            
                    <div>
                        <p>Como señal de la reserva, se realizará ahora el pago de {menu.deposit}€</p>
                        <p>El monto restante se pagará en el restaurante</p>
                    </div>
                    <div className="form-group row">
                        <div className="offset-md-3 col-md-1">
                            <PayPalReserve deposit={menu.deposit} menu={menu} companyId={companyId} reservationDate={reservationDate} periodType={periodType} diners={diners}/>
                        </div>
                    </div>
                </div>
            :
                <div>
                    <p>En este restaurante no se paga un depósito. Se pagará el precio total en el local</p>
                    <form ref={node => form = node}
                        className="needs-validation" noValidate 
                        onSubmit={(e) => handleSubmit(e)}>
                        <div className="form-group row">
                            <div className="offset-md-3 col-md-1">
                                <button type="submit" className="btn btn-primary">
                                    Reservar
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            }    
        </div>
    );

}

export default ReservationDetails;
