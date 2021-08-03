import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindUserReserves = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        console.log("Antes dispatch");
        dispatch(actions.findUserReserves({page: 0}));
        console.log("Despues dispatch");
        history.push('/reservation/find-user-reserves-result');

    });

    return null;

}

export default FindUserReserves;