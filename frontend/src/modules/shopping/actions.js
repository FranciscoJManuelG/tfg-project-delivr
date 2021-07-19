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

const findShoppingCartProductsCompleted = shoppingCart => ({
    type: actionTypes.FIND_SHOPPING_CART_PRODUCTS_COMPLETED,
    shoppingCart
});

export const findShoppingCartProducts = (shoppingCartId, companyId, 
    onSuccess) => dispatch => {
        backend.shoppingService.findShoppingCartProducts(shoppingCartId, companyId,
            shoppingCart => {
                dispatch(findShoppingCartProductsCompleted(shoppingCart));
                onSuccess();
            });
    }

export const changeShoppingCartHomeSale = (shoppingCartId, companyId, homeSale, 
    onSuccess, onErrors) => dispatch => 
    backend.shoppingService.changeShoppingCartHomeSale(shoppingCartId, companyId, homeSale,
        shoppingCart => {
            dispatch(shoppingCartUpdated(shoppingCart));
            onSuccess();
        },
        onErrors);

const buyCompleted = (orderId) => ({
    type: actionTypes.BUY_COMPLETED,
    orderId
});

export const buy = (shoppingCartId, companyId, homeSale, street, cp, codeDiscount,
    onSuccess, onErrors) => dispatch =>
    backend.shoppingService.buy(shoppingCartId, companyId, homeSale, street, cp, codeDiscount, ({id}) => {
        dispatch(buyCompleted(id));
        onSuccess();
    },
    onErrors);

const findOrdersCompleted = orderSearch => ({
    type: actionTypes.FIND_ORDERS_COMPLETED,
    orderSearch
});

const clearOrderSearch = () => ({
    type: actionTypes.CLEAR_ORDER_SEARCH
});

export const findUserOrders = criteria => dispatch => {

    dispatch(clearOrderSearch());
    backend.shoppingService.findUserOrders(criteria, 
        result => dispatch(findOrdersCompleted({criteria, result})));

}    

export const previousFindUserOrdersResultPage = criteria => 
    findUserOrders({page: criteria.page-1});

export const nextFindUserOrdersResultPage = criteria => 
    findUserOrders({page: criteria.page+1});

export const findCompanyOrders = (companyId, criteria) => dispatch => {

    dispatch(clearOrderSearch());
    backend.shoppingService.findCompanyOrders(companyId, criteria, 
        result => dispatch(findOrdersCompleted({criteria, result})));

}    

export const previousFindCompanyOrdersResultPage = (companyId, criteria) => 
    findCompanyOrders(companyId, {page: criteria.page-1});

export const nextFindCompanyOrdersResultPage = (companyId, criteria) => 
    findCompanyOrders(companyId, {page: criteria.page+1});

const findOrderCompleted = order => ({
    type: actionTypes.FIND_ORDER_COMPLETED,
    order
});

export const clearOrder = () => ({
    type: actionTypes.CLEAR_ORDER
});

export const findOrder = orderId => dispatch => {
    backend.shoppingService.findOrder(orderId, order => {
        dispatch(findOrderCompleted(order));
    });
}

const redeemDiscountTicketCompleted = (price) => ({
    type: actionTypes.REDEEM_DISCOUNT_TICKET_COMPLETED,
    price
});

export const redeemDiscountTicket = (companyId, shoppingCartId, code, onSuccess, onErrors) => dispatch =>
    backend.shoppingService.redeemDiscountTicket(companyId, shoppingCartId, code, ({price}) => {
        dispatch(redeemDiscountTicketCompleted(price));
        onSuccess();
    },
    onErrors);

const findDiscountTicketsCompleted = discountTicketSearch => ({
    type: actionTypes.FIND_DISCOUNT_TICKETS_COMPLETED,
    discountTicketSearch
});

const clearDiscountTicketSearch = () => ({
    type: actionTypes.CLEAR_DISCOUNT_TICKET_SEARCH
});

export const findDiscountTickets = (criteria) => dispatch => {

    dispatch(clearDiscountTicketSearch());
    backend.shoppingService.findUserDiscountTickets(criteria,
        result => dispatch(findDiscountTicketsCompleted({criteria, result})));

}      

export const previousFindDiscountTicketsResultPage = (criteria) => 
    findDiscountTickets({page: criteria.page-1});

export const nextFindDiscountTicketsResultPage = (criteria) => 
    findDiscountTickets({page: criteria.page+1});

