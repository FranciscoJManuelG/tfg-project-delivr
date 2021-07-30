import {combineReducers} from 'redux';

import users from '../users';
import * as actionTypes from './actionTypes';

const initialState = {
    companyCategories: null,
    company: null,
    cities: null,
    companyAddressSearch: null,
    goal: null,
    goalTypes: null,
    goalSearch: null,
};

const company = (state = initialState.company, action) => {

    switch (action.type) {

        case actionTypes.ADDED_COMPANY_COMPLETED:
            return action.company;
        
        case actionTypes.MODIFY_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.BLOCK_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.UNLOCK_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.FIND_COMPANY_COMPLETED:
            return action.company;
    
        case users.actionTypes.LOGOUT:
            return initialState.company;

        default:
            return state;

    }

}

const companyCategories = (state = initialState.companyCategories, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED:
            return action.companyCategories;

        default:
            return state;

    }

}

const companyAddressSearch = (state = initialState.companyAddressSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANY_ADDRESSES_COMPLETED:
            return action.companyAddressSearch;

        case actionTypes.CLEAR_COMPANY_ADDRESS_SEARCH:
            return initialState.companyAddressSearch;

        case actionTypes.COMPANY_ADDRESS_DELETED:
            return { criteria: state.criteria, 
                result: {
                     items: state.result.items.filter
                        (companyAddress => companyAddress.id !== action.addressId), 
                    existMoreItems : state.result.existMoreItems} };

        default:
            return state;

    }

}

const cities = (state = initialState.cities, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_CITIES_COMPLETED:
            return action.cities;

        default:
            return state;

    }

}

const goal = (state = initialState.goal, action) => {
    switch(action.type){
        case actionTypes.ADDED_GOAL_COMPLETED:
            return action.goal;

        case actionTypes.FIND_GOAL_COMPLETED:
            return action.goal; 

        default:
            return state;
    }
}

const goalTypes = (state = initialState.goalTypes, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_GOAL_TYPES_COMPLETED:
            return action.goalTypes;

        default:
            return state;

    }

}

const goalSearch = (state = initialState.goalSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_GOALS_COMPLETED:
            return action.goalSearch;

        case actionTypes.CLEAR_GOAL_SEARCH:
            return initialState.goalSearch;

        case actionTypes.MODIFIED_GOAL_STATE_COMPLETED:
            return {criteria: state.criteria,
                result: {
                    items: state.result.items.map(
                        function(goal) { return goal.id === action.goal.id ? action.goal : goal; }
                    ),
                    existMoreItems: state.result.existMoreItems
                }} ;

        default:
            return state;

    }

}

const reducer = combineReducers({
    companyCategories,
    company,
    cities,
    companyAddressSearch,
    goal,
    goalTypes,
    goalSearch
});

export default reducer;

