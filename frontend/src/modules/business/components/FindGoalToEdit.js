import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory, useParams} from 'react-router-dom';

import * as businessSelectors from '../selectors';
import * as actions from '../actions';

const FindGoalToEdit = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();
    const {id} = useParams();

    useEffect(() => {

        dispatch(actions.findGoal(id, company.id));
        history.push(`/business/edit-goal`);
    
    }, [id, company.id, dispatch, history]);

    return null;

}

export default FindGoalToEdit;
