const getModuleState = state => state.shopping;

export const getShoppingCart = state => 
    getModuleState(state).shoppingCart;
