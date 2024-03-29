import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindUserReserves = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findUserReserves({page: 0}));
        history.push('/reservation/find-user-reserves-result');

    });

    return null;

}

export default FindUserReserves;