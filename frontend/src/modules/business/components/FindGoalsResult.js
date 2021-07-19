import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import * as businessSelectors from '../selectors';
import {Pager} from '../../common';
import Goals from './Goals';
import Sidebar from '../../common/components/BusinessSidebar'

const FindGoalsResult = () => {

    const company = useSelector(businessSelectors.getCompany);
    const goalSearch = useSelector(selectors.getGoalSearch);
    const goalTypes = useSelector(selectors.getGoalTypes);
    const dispatch = useDispatch();

    if (!goalSearch) {
        return null;
    }

    if (goalSearch.result.items.length === 0) {
        return (
            <div>
                <Sidebar/>
                <div className="alert alert-info" role="alert">
                    No se ha establecido ningún objetivo para generar descuentos a los clientes
                </div>
            </div>
        );
    }

    return (

        <div>
            <Sidebar/>
            <Goals goalList = {goalSearch.result.items} goalTypes= {goalTypes} company={company}
                onChangeStateItem={(...args) => dispatch(actions.changeStateGoal(...args))}/>
            <Pager 
                back={{
                    enabled: goalSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindGoalsResultPage(company.id, goalSearch.criteria))}}
                next={{
                    enabled: goalSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindGoalsResultPage(company.id, goalSearch.criteria))}}/>
        </div>

    );

}

export default FindGoalsResult;
