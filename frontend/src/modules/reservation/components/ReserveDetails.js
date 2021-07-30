import React, {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedDate, FormattedTime} from 'react-intl';
import {useParams} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';
import ShoppingItemList from './MenuItemList';

const OrderDetails = () => {

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
            </div>

            <ShoppingItemList list={reserve}/>

        </div>

    );

}

export default OrderDetails;
