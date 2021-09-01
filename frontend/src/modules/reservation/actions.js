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

export const reservation = (menuId, companyId, reservationDate, periodType, diners, saleId,
    onSuccess, onErrors) => dispatch =>
    backend.reservationService.reservation(menuId, companyId, reservationDate, diners, periodType, saleId, ({id}) => {
        dispatch(reservationCompleted(id));
        onSuccess();
    },
    onErrors);

const cancelReservationCompleted = (reserveId) => ({
    type: actionTypes.CANCEL_RESERVATION_COMPLETED,
    reserveId
});

export const cancelReservation = (reserveId, onSuccess, onErrors) => dispatch =>
    backend.reservationService.cancelReservation(reserveId,
        () => {
            dispatch(cancelReservationCompleted(reserveId));
            onSuccess();
        },
        onErrors);

const removeReservationCompleted = (reserveId) => ({
    type: actionTypes.REMOVE_RESERVATION_COMPLETED,
    reserveId
});

export const removeReservation = (reserveId, onSuccess, onErrors) => dispatch =>
    backend.reservationService.removeReservation(reserveId,
        () => {
            dispatch(removeReservationCompleted(reserveId));
            onSuccess();
        },
        onErrors);

const calculateDepositCompleted = (price) => ({
    type: actionTypes.CALCULATE_DEPOSIT_COMPLETED,
    price
});

export const calculateDeposit = (companyId, totalPrice, onSuccess) => dispatch =>
    backend.reservationService.calculateDepositFromPercentage(companyId, totalPrice, ({price}) => {
        dispatch(calculateDepositCompleted(price));
        onSuccess();
    });

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

export const findCompanyReservesCanceled = (companyId, criteria) => dispatch => {
    dispatch(clearReserveSearch());
    backend.reservationService.findCompanyReservesCanceled(companyId, criteria, 
        result => dispatch(findReservesCompleted({criteria, result})));

}    

export const previousFindCompanyReservesCanceledResultPage = (companyId, criteria) => 
    findCompanyReservesCanceled(companyId, {page: criteria.page-1});

export const nextFindCompanyReservesCanceledResultPage = (companyId, criteria) => 
    findCompanyReservesCanceled(companyId, {page: criteria.page+1});

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

const addEventEvaluationCompleted = () => ({
    type: actionTypes.ADD_EVENT_EVALUATION_COMPLETED
});

export const addEventEvaluation = (eventEvaluationId, points, opinion,
    onSuccess, onErrors) => dispatch =>
    backend.reservationService.addEventEvaluation(eventEvaluationId,
        points, opinion, () => {
            dispatch(addEventEvaluationCompleted());
            onSuccess();
        },
        onErrors);

const findUserEventEvaluationsCompleted = userEventEvaluationSearch => ({
    type: actionTypes.FIND_USER_EVENT_EVALUATIONS_COMPLETED,
    userEventEvaluationSearch
});

const clearUserEventEvaluationSearch = () => ({
    type: actionTypes.CLEAR_USER_EVENT_EVALUATION_SEARCH
});

export const findUserEventEvaluations = criteria => dispatch => {

    dispatch(clearUserEventEvaluationSearch());
    backend.reservationService.findUserEventEvaluations(criteria,
        result => dispatch(findUserEventEvaluationsCompleted({criteria, result})));
}    

export const previousFindUserEventEvaluationsResultPage = criteria => 
    findUserEventEvaluations({page: criteria.page-1});

export const nextFindUserEventEvaluationsResultPage = criteria => 
    findUserEventEvaluations({page: criteria.page+1});

const findCompanyEventEvaluationsCompleted = companyEventEvaluationSearch => ({
    type: actionTypes.FIND_COMPANY_EVENT_EVALUATIONS_COMPLETED,
    companyEventEvaluationSearch
});

const clearCompanyEventEvaluationSearch = () => ({
    type: actionTypes.CLEAR_COMPANY_EVENT_EVALUATION_SEARCH
});

export const findCompanyEventEvaluations = (companyId, criteria) => dispatch => {

    dispatch(clearCompanyEventEvaluationSearch());
    backend.reservationService.findCompanyEventEvaluations(companyId, criteria,
        result => dispatch(findCompanyEventEvaluationsCompleted({criteria, result})));

}    

export const previousFindCompanyEventEvaluationsResultPage = (companyId, criteria) => 
    findCompanyEventEvaluations(companyId, {page: criteria.page-1});

export const nextFindCompanyEventEvaluationsResultPage = (companyId, criteria) => 
    findCompanyEventEvaluations(companyId, {page: criteria.page+1});