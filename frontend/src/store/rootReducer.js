import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import business from '../modules/business';
import businessCatalog from '../modules/businessCatalog';

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    business: business.reducer,
    businessCatalog: businessCatalog.reducer,
});

export default rootReducer;
