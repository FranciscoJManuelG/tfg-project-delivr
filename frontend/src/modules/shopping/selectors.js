const getModuleState = state => state.shopping;

export const getShoppingCart = state => 
    getModuleState(state).shoppingCart;

export const getLastOrderId = state =>
    getModuleState(state).lastOrderId;

export const getOrderSearch = state =>
    getModuleState(state).orderSearch;

export const getOrder = state =>
    getModuleState(state).order;

export const getLastAddress = state =>
    getModuleState(state).lastAddress;

export const getDiscountTicketSearch = state =>
    getModuleState(state).discountTicketSearch;
