import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as businessSelectors from '../../business/selectors';

const FindCompanyOrders = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findCompanyOrders(company.id, {page: 0}));
        history.push('/shopping/find-company-orders-result');

    });

    return null;

}

export default FindCompanyOrders;
