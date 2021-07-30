import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindUserOrders = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findUserReserves({page: 0}));
        history.push('/shopping/find-user-orders-result');

    });

    return null;

}

export default FindUserOrders;