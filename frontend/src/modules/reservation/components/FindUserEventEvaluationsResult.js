import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import UserEventEvaluations from './UserEventEvaluations';

const FindUserEventEvaluationsResult = () => {

    const userEventEvaluationSearch = useSelector(selectors.getUserEventEvaluationSearch);
    const dispatch = useDispatch();

    if (!userEventEvaluationSearch) {
        return null;
    }

    if (userEventEvaluationSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
               No hay valoraciones ha realizar aún.
            </div>
        );
    }

    return (

        <div>
            <UserEventEvaluations userEventEvaluations={userEventEvaluationSearch.result.items}/>
            <Pager 
                back={{
                    enabled: userEventEvaluationSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindUserEventEvaluationsResultPage(userEventEvaluationSearch.criteria))}}
                next={{
                    enabled: userEventEvaluationSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindUserEventEvaluationsResultPage(userEventEvaluationSearch.criteria))}}/>
        </div>

    );

}

export default FindUserEventEvaluationsResult;
