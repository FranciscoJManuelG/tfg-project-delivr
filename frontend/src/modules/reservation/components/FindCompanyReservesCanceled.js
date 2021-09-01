import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as businessSelectors from '../../business/selectors';

const FindCompanyReservesCanceled = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findCompanyReservesCanceled(company.id, {page: 0}));
        history.push('/reservation/find-company-reserves-canceled-result');

    });

    return null;

}

export default FindCompanyReservesCanceled;
