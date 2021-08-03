import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindUserEventEvaluations = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findUserEventEvaluations({page: 0}));
        history.push('/reservation/find-user-event-evaluations-result');

    });

    return null;

}

export default FindUserEventEvaluations;