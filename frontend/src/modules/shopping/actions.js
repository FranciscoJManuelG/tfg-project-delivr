import backend from '../../backend';
import * as actionTypes from './actionTypes';

const shoppingCartUpdated = shoppingCart => ({
    type: actionTypes.SHOPPING_CART_UPDATED,
    shoppingCart
});

export const addToShoppingCart = (shoppingCartId, productId, companyId, quantity,
    onSuccess, onErrors) => dispatch =>
    backend.shoppingService.addToShoppingCart(shoppingCartId, productId,
        companyId, quantity, shoppingCart => {
            dispatch(shoppingCartUpdated(shoppingCart));
            onSuccess();
        },
        onErrors);

export const updateShoppingCartItemQuantity = (shoppingCartId, productId, 
    companyId, quantity, onSuccess, onErrors) => dispatch => 
    backend.shoppingService.updateShoppingCartItemQuantity(shoppingCartId,
        productId, companyId, quantity, shoppingCart => {
            dispatch(shoppingCartUpdated(shoppingCart));
            onSuccess();
        },
        onErrors);

export const removeShoppingCartItem = (shoppingCartId, productId, companyId, 
    onSuccess, onErrors) => dispatch => 
    backend.shoppingService.removeShoppingCartItem(shoppingCartId,
        productId, companyId, 
        shoppingCart => {
            dispatch(shoppingCartUpdated(shoppingCart));
            onSuccess();
        },
        onErrors);

const clearShoppingCart = () => ({
    type: actionTypes.CLEAR_SHOPPING_CART
});

const findShoppingCartProductsCompleted = shoppingCart => ({
    type: actionTypes.FIND_SHOPPING_CART_PRODUCTS_COMPLETED,
    shoppingCart
});

export const findShoppingCartProducts = (shoppingCartId, companyId, 
    onSuccess) => dispatch => {
        dispatch(clearShoppingCart());
        backend.shoppingService.findShoppingCartProducts(shoppingCartId, companyId,
            shoppingCart => {
                dispatch(findShoppingCartProductsCompleted(shoppingCart));
                onSuccess();
            });
    }


