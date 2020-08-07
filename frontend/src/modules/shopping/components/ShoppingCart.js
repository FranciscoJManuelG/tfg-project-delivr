import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import ShoppingItemList from './ShoppingItemList';
import * as selectors from '../selectors';
import * as actions from '../actions';

const ShoppingCart = ({companyId}) => {

    const cart = useSelector(selectors.getShoppingCart);
    const dispatch = useDispatch();

    return (

        <div>
            <ShoppingItemList list={cart} edit companyId = {companyId}
                onUpdateQuantity={(...args) => dispatch(actions.updateShoppingCartItemQuantity(...args))}
                onRemoveItem={(...args) => dispatch(actions.removeShoppingCartItem(...args))}/>
        </div>

    );

}
export default ShoppingCart;
