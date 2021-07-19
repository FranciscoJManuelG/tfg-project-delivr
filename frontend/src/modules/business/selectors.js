const getModuleState = state => state.business

export const getCompany = state => 
    getModuleState(state).company;

export const getCompanyCategories = state =>
    getModuleState(state).companyCategories;

export const getCompanyCategoryName = (companyCategories, id) => {
    
    if (!companyCategories){
        return '';
    }
    
    const companyCategory = companyCategories.find(companyCategory => companyCategory.id === id);
    
    if (!companyCategory){
        return '';
    }

    return companyCategory.name;
}

export const getCompanyAddressSearch = state =>
    getModuleState(state).companyAddressSearch;

export const getCities = state =>
    getModuleState(state).cities;

export const getCityName = (cities, id) => {

    if (!cities) {
        return '';
    }

    const city = cities.find(city => city.id === id);

    if (!city) {
        return '';
    }

    return city.name;

}

export const existsCompany = state =>
    getCompany(state) !== null

export const getBlock = state => 
    existsCompany(state) ? getCompany(state).block : null;

export const getGoalTypes = state =>
    getModuleState(state).goalTypes;

export const getGoalTypeGoalName = (goalTypes, id) => {
    
    if (!goalTypes){
        return '';
    }
    
    const goalType = goalTypes.find(goalType => goalType.id === id);
    
    if (!goalType){
        return '';
    }

    return goalType.goalName;
    
}

export const getGoalSearch = state =>
    getModuleState(state).goalSearch;

export const getGoal = state => 
    getModuleState(state).goal;
