import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';

const BlockCompany = () => {

    const company = useSelector(selectors.getCompany);
    const history = useHistory();
    const dispatch = useDispatch();

    useEffect(() => {

        dispatch(actions.blockCompany(company.id,
            () => history.push('/')));

    });

    return null;
}

export default BlockCompany;