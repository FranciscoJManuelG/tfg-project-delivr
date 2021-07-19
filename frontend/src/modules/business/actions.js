import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from './selectors';

const addedCompanyCompleted = company => ({
    type: actionTypes.ADDED_COMPANY_COMPLETED,
    company
});

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
    onSuccess, onErrors) => dispatch =>
    backend.businessService.addCompany(name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
        company => {
            dispatch(addedCompanyCompleted(company));
            onSuccess();
        },
        onErrors);

const modifyCompanyCompleted = company => ({
    type: actionTypes.MODIFY_COMPANY_COMPLETED,
    company
})

export const modifyCompany = (company, onSuccess, onErrors) => dispatch =>
    backend.businessService.modifyCompany(company, 
        company => {
            dispatch(modifyCompanyCompleted(company));
            onSuccess();
        },
        onErrors);

const blockCompanyCompleted = company => ({
    type: actionTypes.BLOCK_COMPANY_COMPLETED,
    company
})

export const blockCompany = (companyId, onSuccess) => dispatch =>
    backend.businessService.blockCompany(companyId, 
    company => {
        dispatch(blockCompanyCompleted(company));
        onSuccess();
    });

const unlockCompanyCompleted = company => ({
    type: actionTypes.UNLOCK_COMPANY_COMPLETED,
    company
})

export const unlockCompany = (companyId, onSuccess) => dispatch =>
    backend.businessService.unlockCompany(companyId, 
    company => {
        dispatch(unlockCompanyCompleted(company));
        onSuccess();
    });

const findAllCompanyCategoriesCompleted = companyCategories => ({
    type: actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED,
    companyCategories
});

export const findAllCompanyCategories = () => (dispatch, getState) => {

    const companyCategories = selectors.getCompanyCategories(getState());

    if (!companyCategories) {

        backend.businessService.findAllCompanyCategories(
            companyCategories => dispatch(findAllCompanyCategoriesCompleted(companyCategories))
        );

    }

}

const findCompanyAddressesCompleted = companyAddressSearch => ({
    type: actionTypes.FIND_COMPANY_ADDRESSES_COMPLETED,
    companyAddressSearch
});

const clearCompanyAddressSearch = () => ({
    type: actionTypes.CLEAR_COMPANY_ADDRESS_SEARCH
});


export const findCompanyAddresses = (companyId, criteria) => dispatch => {

    dispatch(clearCompanyAddressSearch());
    backend.businessService.findCompanyAddresses(companyId, criteria, 
        result => dispatch(findCompanyAddressesCompleted({criteria, result})));

}

export const previousFindCompanyAddressesResultPage = (companyId, criteria) => 
    findCompanyAddresses(companyId, {page: criteria.page-1});

export const nextFindCompanyAddressesResultPage = (companyId, criteria) => 
    findCompanyAddresses(companyId, {page: criteria.page+1});

const addedCompanyAddressCompleted = address => ({
    type: actionTypes.ADDED_COMPANY_ADDRESS_COMPLETED,
    address
});

export const addCompanyAddress = (street, cp, cityId, companyId, onSuccess, onErrors) => dispatch =>
    backend.businessService.addCompanyAddress(street, cp, cityId, companyId, 
        address => {
            dispatch(addedCompanyAddressCompleted(address));
            onSuccess();
        },
        onErrors);

const companyAddressDeleted = addressId => ({
    type: actionTypes.COMPANY_ADDRESS_DELETED,
    addressId
});

export const deleteCompanyAddress = (addressId, onSuccess, 
    onErrors) => dispatch => 
    backend.businessService.deleteCompanyAddress(addressId,
        () => {
            dispatch(companyAddressDeleted(addressId));
            onSuccess();
        },
        onErrors);

const findAllCitiesCompleted = cities => ({
    type: actionTypes.FIND_ALL_CITIES_COMPLETED,
    cities
});

export const findAllCities = () => (dispatch, getState) => {

    const cities = selectors.getCities(getState());

    if (!cities) {

        backend.businessService.findAllCities(
            cities => dispatch(findAllCitiesCompleted(cities))
        );

    }

}

export const findCompanyCompleted = company => ({
    type: actionTypes.FIND_COMPANY_COMPLETED,
    company
})

const addedGoalCompleted = goal => ({
    type: actionTypes.ADDED_GOAL_COMPLETED,
    goal
});

export const addGoal = (companyId, discountType, discountCash, discountPercentage, goalTypeId, goalQuantity,
    onSuccess, onErrors) => dispatch =>
    backend.businessService.addGoal(companyId, discountType, discountCash, discountPercentage, goalTypeId, goalQuantity,
        goal => {
            dispatch(addedGoalCompleted(goal));
            onSuccess();
        },
        onErrors);

const editGoalCompleted = goal => ({
    type: actionTypes.EDIT_GOAL_COMPLETED,
    goal
})
                
export const editGoal = (goal, companyId, onSuccess, onErrors) => dispatch =>
    backend.businessService.modifyGoal(goal, companyId,
        goal => {
            dispatch(editGoalCompleted(goal));
            onSuccess();
        },
        onErrors);
        

const findAllGoalTypesCompleted = goalTypes => ({
    type: actionTypes.FIND_ALL_GOAL_TYPES_COMPLETED,
    goalTypes
});

export const findAllGoalTypes = () => (dispatch, getState) => {

    const goalTypes = selectors.getGoalTypes(getState());

    if (!goalTypes) {

        backend.businessService.findAllGoalTypes(
            goalTypes => dispatch(findAllGoalTypesCompleted(goalTypes))
        );

    }

}

const findGoalsCompleted = goalSearch => ({
    type: actionTypes.FIND_GOALS_COMPLETED,
    goalSearch
});

const clearGoalSearch = () => ({
    type: actionTypes.CLEAR_GOAL_SEARCH
});

export const findGoals = (companyId, criteria) => dispatch => {

    dispatch(clearGoalSearch());
    backend.businessService.findCompanyGoals(companyId, criteria,
        result => dispatch(findGoalsCompleted({criteria, result})));

}      

export const previousFindGoalsResultPage = (companyId, criteria) => 
    findGoals(companyId, {page: criteria.page-1});

export const nextFindGoalsResultPage = (companyId, criteria) => 
    findGoals(companyId, {page: criteria.page+1});

const modifiedGoalStateCompleted = goal => ({
    type: actionTypes.MODIFIED_GOAL_STATE_COMPLETED,
    goal
});

export const changeStateGoal = (goalId, companyId, option, onSuccess) => dispatch =>
    backend.businessService.modifyStateGoal(goalId, companyId, option, 
    goal => {
        dispatch(modifiedGoalStateCompleted(goal));
        onSuccess();
    });

const findGoalCompleted = goal => ({
    type: actionTypes.FIND_GOAL_COMPLETED,
    goal
});

export const clearGoal = () => ({
    type: actionTypes.CLEAR_GOAL
});

export const findGoal = (goalId, companyId) => dispatch => {
    dispatch(clearGoal());
    backend.businessService.findGoal(goalId, companyId,
        goal => {
            dispatch(findGoalCompleted(goal))
        });
}
        