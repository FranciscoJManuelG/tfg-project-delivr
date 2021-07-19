import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindDiscountTickets = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findDiscountTickets({page: 0}));
        history.push('/shopping/find-discount-tickets-result');

    });

    return null;

}

export default FindDiscountTickets;