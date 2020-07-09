import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import logger from 'redux-logger';

import rootReducer from './rootReducer';

const configureStore = () => {

    const middlewares = [thunk];

    if (process.env.NODE_ENV !== 'production') {    
        middlewares.push(logger);
    }

    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

    return createStore(rootReducer, composeEnhancers(
       applyMiddleware(...middlewares)
    ));

}

export default configureStore;