import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import business from '../modules/business';

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    business: business.reducer,
});

export default rootReducer;
