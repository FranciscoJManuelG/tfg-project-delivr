import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import Reserves from './Reserves';

const FindReservesResult = () => {

    const reserveSearch = useSelector(selectors.getReserveSearch);
    const dispatch = useDispatch();

    if (!reserveSearch) {
        return null;
    }

    if (reserveSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No has realizado ninguna reserva aún.
            </div>
        );
    }

    return (

        <div>
            <Reserves reserves={reserveSearch.result.items}
                onCancelReservation={(...args) => dispatch(actions.cancelReservation(...args))}/>
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

export default FindReservesResult;
