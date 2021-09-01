import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import * as businessSelectors from '../../business/selectors';
import {Pager} from '../../common';
import ReservesCanceled from './ReservesCanceled';

const FindCompanyReservesCanceledResult = () => {

    const company = useSelector(businessSelectors.getCompany);
    const reserveSearch = useSelector(selectors.getReserveSearch);
    const dispatch = useDispatch();

    if (!reserveSearch) {
        return null;
    }

    if (reserveSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                No hay ninguna cancelación
            </div>
        );
    }

    return (

        <div>
            <ReservesCanceled reserves={reserveSearch.result.items}/>
            <Pager 
                back={{
                    enabled: reserveSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindCompanyReservesCanceledResultPage(company.id, reserveSearch.criteria))}}
                next={{
                    enabled: reserveSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindCompanyReservesCanceledResultPage(company.id, reserveSearch.criteria))}}/>
        </div>

    );

}

export default FindCompanyReservesCanceledResult;
