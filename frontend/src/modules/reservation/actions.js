import backend from '../../backend';
import * as actionTypes from './actionTypes';

const menuUpdated = menu => ({
    type: actionTypes.MENU_UPDATED,
    menu
});

export const addToMenu = (menuId, productId, companyId, quantity,
    onSuccess, onErrors) => dispatch =>
    backend.reservationService.addToMenu(menuId, productId,
        companyId, quantity, menu => {
            dispatch(menuUpdated(menu));
            onSuccess();
        },
        onErrors);

export const updateMenuItemQuantity = (menuId, productId, 
    companyId, quantity, onSuccess, onErrors) => dispatch => 
    backend.reservationService.updateMenuItemQuantity(menuId,
        productId, companyId, quantity, menu => {
            dispatch(menuUpdated(menu));
            onSuccess();
        },
        onErrors);

export const removeMenuItem = (menuId, productId, companyId, 
    onSuccess, onErrors) => dispatch => 
    backend.reservationService.removeMenuItem(menuId,
        productId, companyId, 
        menu => {
            dispatch(menuUpdated(menu));
            onSuccess();
        },
        onErrors);

const findMenuProductsCompleted = menu => ({
    type: actionTypes.FIND_MENU_PRODUCTS_COMPLETED,
    menu
});

export const findMenuProducts = (menuId, companyId, 
    onSuccess) => dispatch => {
        backend.reservationService.findMenuProducts(menuId, companyId,
            menu => {
                dispatch(findMenuProductsCompleted(menu));
                onSuccess();
            });
    }

const reservationCompleted = (reserveId) => ({
    type: actionTypes.RESERVATION_COMPLETED,
    reserveId
});

export const reservation = (menuId, companyId, reservationDate, periodType, diners,
    onSuccess, onErrors) => dispatch =>
    backend.reservationService.reservation(menuId, companyId, reservationDate, diners, periodType, ({id}) => {
        dispatch(reservationCompleted(id));
        onSuccess();
    },
    onErrors);

const checkCapacityCompleted = () => ({
    type: actionTypes.CHECK_CAPACITY_COMPLETED
});

export const checkCapacity = (companyId, reservationDate, periodType, diners,
    onSuccess, onErrors) => dispatch =>
        
    backend.reservationService.checkCapacity(companyId, reservationDate, periodType, diners, () => {
        dispatch(checkCapacityCompleted());
        onSuccess();
    },
    onErrors);

const findReservesCompleted = reserveSearch => ({
    type: actionTypes.FIND_RESERVES_COMPLETED,
    reserveSearch
});

const clearReserveSearch = () => ({
    type: actionTypes.CLEAR_RESERVE_SEARCH
});

export const findUserReserves = criteria => dispatch => {

    dispatch(clearReserveSearch());
    backend.reservationService.findUserReserves(criteria, 
        result => dispatch(findReservesCompleted({criteria, result})));

}    

export const previousFindUserReservesResultPage = criteria => 
    findUserReserves({page: criteria.page-1});

export const nextFindUserReservesResultPage = criteria => 
    findUserReserves({page: criteria.page+1});

export const findCompanyReserves = (companyId, reservationDate, periodType, criteria) => dispatch => {

    dispatch(clearReserveSearch());
    backend.reservationService.findCompanyReserves(companyId, reservationDate, periodType, criteria, 
        result => dispatch(findReservesCompleted({criteria, result})));

}    

export const previousFindCompanyReservesResultPage = (companyId, reservationDate, periodType, criteria) => 
    findCompanyReserves(companyId, reservationDate, periodType, {page: criteria.page-1});

export const nextFindCompanyReservesResultPage = (companyId, reservationDate, periodType, criteria) => 
    findCompanyReserves(companyId, reservationDate, periodType, {page: criteria.page+1});

const findReserveCompleted = reserve => ({
    type: actionTypes.FIND_RESERVE_COMPLETED,
    reserve
});

export const clearReserve = () => ({
    type: actionTypes.CLEAR_RESERVE
});

export const findReserve = reserveId => dispatch => {
    backend.reservationService.findReserve(reserveId, reserve => {
        dispatch(findReserveCompleted(reserve));
    });
}