import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import business from '../modules/business';
import businessCatalog from '../modules/businessCatalog';
import productCatalog from '../modules/productCatalog';
import productManagement from '../modules/productManagement';
import shopping from '../modules/shopping';
import reservation from '../modules/reservation';

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    business: business.reducer,
    businessCatalog: businessCatalog.reducer,
    productCatalog: productCatalog.reducer,
    productManagement: productManagement.reducer,
    shopping: shopping.reducer,
    reservation: reservation.reducer,
});

export default rootReducer;
