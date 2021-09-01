import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';

const CalculateDeposit = () => {

    const menu = useSelector(selectors.getMenu);
    const dispatch = useDispatch();
    const history = useHistory();
    const {id, reservationDate, periodType, diners} = useParams();

    useEffect(() => {

        dispatch(actions.calculateDeposit(id, menu.totalPrice));
        history.push(`/reservation/reservation-details/${id}/${reservationDate}/${periodType}/${diners}`);

    });

    return null;

}

export default CalculateDeposit;
