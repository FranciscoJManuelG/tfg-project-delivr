import {config, appFetch} from './appFetch';

export const addToMenu = (menuId, productId, companyId, quantity, onSuccess, 
    onErrors) =>
    appFetch(`/reservation/menus/${menuId}/addToMenu`, 
        config('POST', {productId, companyId, quantity}), onSuccess, onErrors);

export const updateMenuItemQuantity = (menuId, productId, companyId,
    quantity, onSuccess, onErrors) =>
    appFetch(`/reservation/menus/${menuId}/updateMenuItemQuantity`, 
        config('POST', {productId, companyId, quantity}), onSuccess, onErrors);

export const removeMenuItem = (menuId, productId, companyId, onSuccess,
    onErrors) =>
    appFetch(`/reservation/menus/${menuId}/removeMenuItem`, 
        config('POST', {productId, companyId}), onSuccess, onErrors);

export const findMenuProducts = (menuId, companyId,  onSuccess) => 
    appFetch(`/reservation/menus/${menuId}?companyId=${companyId}`, 
        config('GET'), onSuccess);

export const reservation = (menuId, companyId, reservationDate, diners, periodType, saleId,
    onSuccess, onErrors) =>
    appFetch(`/reservation/menus/${menuId}/reservation`, 
        config('POST', {companyId, reservationDate, diners, periodType, saleId}), onSuccess, onErrors);

export const cancelReservation = (reserveId, onSuccess, onErrors) =>
    appFetch(`/reservation/reserves/${reserveId}`,
        config('DELETE'), onSuccess, onErrors);

export const calculateDepositFromPercentage = (companyId, totalPrice, onSuccess) => 
    appFetch(`/reservation/menus/calculateDeposit?companyId=${companyId}&totalPrice=${totalPrice}`, 
        config('GET'), onSuccess);

export const checkCapacity = (companyId, reservationDate, periodType, diners, onSuccess, onErrors) => 
    appFetch(`/reservation/menus/checkCapacity?companyId=${companyId}&reservationDate=${reservationDate}
        &periodType=${periodType}&diners=${diners}`, 
        config('GET'), onSuccess, onErrors);
        
export const obtainMaxDinersAllowed = (companyId, reservationDate, periodType, onSuccess) => 
    appFetch(`/reservation/menus/obtainMaxDinersAllowed?companyId=${companyId}&reservationDate=${reservationDate}
        &periodType=${periodType}`, 
        config('GET'), onSuccess);

export const findReserve = (reserveId, onSuccess) =>
    appFetch(`/reservation/reserves/${reserveId}`, config('GET'), onSuccess);

export const findUserReserves = ({page}, onSuccess) => 
    appFetch(`/reservation/userReserves?page=${page}`, config('GET'), onSuccess);

export const findCompanyReserves = (companyId, reservationDate, periodType, {page}, onSuccess) => 
    appFetch(`/reservation/companyReserves?companyId=${companyId}&reservationDate=${reservationDate}
        &periodType=${periodType}&page=${page}`, config('GET'), onSuccess);

export const findCompanyReservesCanceled = (companyId, {page}, onSuccess) => 
    appFetch(`/reservation/companyReservesCanceled?companyId=${companyId}&page=${page}`, 
    config('GET'), onSuccess);

export const addEventEvaluation = (eventEvaluationId, points, opinion,
    onSuccess, onErrors) =>
    appFetch(`/reservation/eventEvaluation/${eventEvaluationId}`, 
        config('POST', {eventEvaluationId, points, opinion}), onSuccess, onErrors);

export const findUserEventEvaluations = ({page}, onSuccess) => 
    appFetch(`/reservation/userEventEvaluations?page=${page}`, config('GET'), onSuccess);

export const findCompanyEventEvaluations = (companyId, {page}, onSuccess) => 
    appFetch(`/reservation/companyEventEvaluations?companyId=${companyId}&page=${page}`, 
        config('GET'), onSuccess);
        