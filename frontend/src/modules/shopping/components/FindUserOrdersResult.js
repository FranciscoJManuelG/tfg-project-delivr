import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import Orders from './Orders';

const FindOrdersResult = () => {

    const orderSearch = useSelector(selectors.getOrderSearch);
    const dispatch = useDispatch();

    if (!orderSearch) {
        return null;
    }

    if (orderSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No has realizado ningún pedido aún.
            </div>
        );
    }

    return (

        <div>
            <Orders orders={orderSearch.result.items}/>
            <Pager 
                back={{
                    enabled: orderSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindUserOrdersResultPage(orderSearch.criteria))}}
                next={{
                    enabled: orderSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindUserOrdersResultPage(orderSearch.criteria))}}/>
        </div>

    );

}

export default FindOrdersResult;
