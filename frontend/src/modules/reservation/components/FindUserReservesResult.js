import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import Orders from './Reserves';

const FindOrdersResult = () => {

    const reserveSearch = useSelector(selectors.getReserveSearch);
    const dispatch = useDispatch();

    if (!reserveSearch) {
        return null;
    }

    if (reserveSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No has realizado ningún pedido aún.
            </div>
        );
    }

    return (

        <div>
            <Orders orders={reserveSearch.result.items}/>
            <Pager 
                back={{
                    enabled: reserveSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindUserReservesResultPage(reserveSearch.criteria))}}
                next={{
                    enabled: reserveSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindUserReservesResultPage(reserveSearch.criteria))}}/>
        </div>

    );

}

export default FindOrdersResult;
