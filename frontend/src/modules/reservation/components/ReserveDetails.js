import React, {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedDate} from 'react-intl';
import {useParams} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';
import MenuItemList from './MenuItemList';

const ReserveDetails = () => {

    const {id} = useParams();
    const reserve = useSelector(selectors.getReserve);
    const dispatch = useDispatch();

    useEffect(() => {

        if (!Number.isNaN(id)) {   
            dispatch(actions.findReserve(id));
        }

        return () => dispatch(actions.clearReserve());

    }, [id, dispatch]);

    if (!reserve) {
        return null;
    }

    return (

        <div>
            <div className="card text-center">
                <div className="card-body">
                    <h5 className="card-title">
                        Detalle de la reserva en {reserve.companyName} para {reserve.periodType === 'LUNCH' ?
                            'Comer' : 'Cenar'
                        }
                    </h5>
                    <h6 className="card-text">
                        {reserve.firstName} {reserve.lastName}
                    </h6>
                    <h6 className="card-text">
                        Email: {reserve.email} Teléfono: {reserve.phone}
                    </h6>
                    <h6 className="card-subtitle text-muted">
                        <FormattedDate value={new Date(reserve.date)}/>
                    </h6>
                    <p className="card-text">
                        Mesa reservada para {reserve.diners} persona/s
                    </p>
                </div>
            </div>

            <MenuItemList list={reserve}/>

        </div>

    );

}

export default ReserveDetails;
