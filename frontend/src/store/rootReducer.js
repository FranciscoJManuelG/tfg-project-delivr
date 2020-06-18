import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import companies from '../modules/companies';

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    companies: companies.reducer
});

export default rootReducer;
