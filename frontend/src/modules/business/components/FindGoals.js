import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as businessSelectors from '../selectors';

const FindGoals = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findGoals(company.id, {page: 0}));
        history.push('/business/find-goals-result');

    });

    return null;

}

export default FindGoals;