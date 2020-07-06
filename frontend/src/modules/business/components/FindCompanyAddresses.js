import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as selectors from '../selectors';
import * as actions from '../actions';

const FindCompanyAddresses = () => {

    const company = useSelector(selectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findCompanyAddresses(company.id, {page: 0}));
        history.push('/business/find-company-addresses-result');

    });

    return null;

}

export default FindCompanyAddresses;
