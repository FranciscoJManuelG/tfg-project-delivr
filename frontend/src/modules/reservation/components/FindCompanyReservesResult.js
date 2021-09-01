import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';
import * as businessSelectors from '../../business/selectors';
import {Pager} from '../../common';
import Reserves from './Reserves';

const FindCompanyReservesResult = () => {

    const company = useSelector(businessSelectors.getCompany);
    const reserveSearch = useSelector(selectors.getReserveSearch);
    const dispatch = useDispatch();
    const {reservationDate, periodType} = useParams();

    if (!reserveSearch) {
        return null;
    }

    if (reserveSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No hay ninguna reserva
            </div>
        );
    }

    return (

        <div>
            <Reserves reserves={reserveSearch.result.items}/>
            <Pager 
                back={{
                    enabled: reserveSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindCompanyReservesResultPage(company.id, reservationDate, periodType, reserveSearch.criteria))}}
                next={{
                    enabled: reserveSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindCompanyReservesResultPage(company.id, reservationDate, periodType, reserveSearch.criteria))}}/>
        </div>

    );

}

export default FindCompanyReservesResult;
