import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useParams, useHistory} from 'react-router-dom';

import * as shoppingActions from '../../shopping/actions';
import * as shoppingSelectors from '../../shopping/selectors';

const FindShoppingCartProducts = () => {

    const cart = useSelector(shoppingSelectors.getShoppingCart);
    const dispatch = useDispatch();
    const {id} = useParams();
    const history = useHistory();

    useEffect(() => {

        dispatch(shoppingActions.findShoppingCartProducts(cart.id, id));
        history.push(`/productCatalog/find-products-by-company-result/${id}`);
                       
    }, [id, cart, dispatch, history]);
    
    return null;

}

export default FindShoppingCartProducts; 
