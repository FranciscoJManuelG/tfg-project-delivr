import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import * as shoppingActions from '../../shopping/actions';
import * as shoppingSelectors from '../../shopping/selectors';

const FindShoppingCartProducts = () => {

    const cart = useSelector(shoppingSelectors.getShoppingCart);
    const dispatch = useDispatch();
    const {id, doReserve, cityId} = useParams();
    const history = useHistory();

    useEffect(() => {

        dispatch(shoppingActions.findShoppingCartProducts(cart.id, id));
        history.push(`/productCatalog/find-products-by-company-to-deliver-result/${id}/${doReserve}/${cityId}`);
                       
    }, [id, cart, doReserve, cityId, dispatch, history]);
    
    return null;

}

export default FindShoppingCartProducts; 
