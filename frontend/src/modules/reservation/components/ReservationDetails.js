import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';
import {useParams} from 'react-router-dom';

import {Errors} from '../../common';
import MenuItemList from './MenuItemList';
import * as selectors from '../selectors';
import * as actions from '../actions';
import moment from 'moment';
import 'moment/locale/es';

const ReservationDetails = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const menu = useSelector(selectors.getMenu);
    const [backendErrors, setBackendErrors] = useState(null);
    const {id, reservationDate, periodType, diners} = useParams();
    let form;
    const companyId = id;
    var dateEs = moment(new Date(reservationDate)).format('LL');


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
            <form ref={node => form = node}
                className="needs-validation" noValidate 
                onSubmit={(e) => handleSubmit(e)}>
                <div className="form-group row">
                    <div className="offset-md-3 col-md-1">
                        <button type="submit" className="btn btn-primary">
                            Comprar
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );

}

export default ReservationDetails;
