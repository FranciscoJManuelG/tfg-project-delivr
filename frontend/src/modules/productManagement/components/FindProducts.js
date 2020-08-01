import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as businessSelectors from '../../business/selectors'
import * as actions from '../actions';

const FindProducts = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findProducts(company.id));
        history.push(`/management/find-products-result`);

    });

    return null;

}

export default FindProducts;
