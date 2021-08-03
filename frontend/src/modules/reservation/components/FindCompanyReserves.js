import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as businessSelectors from '../../business/selectors';

const FindCompanyReserves = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();
    const {reservationDate, periodType} = useParams();

    useEffect(() => {

        dispatch(actions.findCompanyReserves(company.id, reservationDate, periodType, {page: 0}));
        history.push('/reservation/find-company-reserves-result');

    });

    return null;

}

export default FindCompanyReserves;
