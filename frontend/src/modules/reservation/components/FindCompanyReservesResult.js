import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import * as businessSelectors from '../../business/selectors';
import {Pager} from '../../common';
import Orders from './Reserves';

const FindCompanyOrdersResult = () => {

    const company = useSelector(businessSelectors.getCompany);
    const reserveSearch = useSelector(selectors.getReserveSearch);
    const dispatch = useDispatch();

    if (!reserveSearch) {
        return null;
    }

    if (reserveSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No hay ningún pedido
            </div>
        );
    }

    return (

        <div>
            <Orders orders={reserveSearch.result.items}/>
            <Pager 
                back={{
                    enabled: reserveSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindCompanyReservesResultPage(company.id, reserveSearch.criteria))}}
                next={{
                    enabled: reserveSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindCompanyReservesResultPage(company.id, reserveSearch.criteria))}}/>
        </div>

    );

}

export default FindCompanyOrdersResult;
