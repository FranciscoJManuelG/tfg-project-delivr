import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';

const FindMenuProducts = () => {

    const menu = useSelector(selectors.getMenu);
    const dispatch = useDispatch();
    const {id, reservationDate, periodType, diners} = useParams();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findMenuProducts(menu.id, id));
        history.push(`/productCatalog/find-products-by-company-for-reservations-result/${id}/${reservationDate}/${periodType}/${diners}`);
                       
    }, [id, reservationDate, periodType, diners, menu, dispatch, history]);
    
    return null;

}

export default FindMenuProducts; 
