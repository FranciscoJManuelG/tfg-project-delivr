import React from 'react';
import {useSelector, useDispatch} from 'react-redux';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import DiscountTickets from './DiscountTickets';

const FindDiscountTicketsResult = () => {

    const discountTicketSearch = useSelector(selectors.getDiscountTicketSearch);
    const dispatch = useDispatch();

    if (!discountTicketSearch) {
        return null;
    }

    if (discountTicketSearch.result.items.length === 0) {
        return (
            <div>
                <div className="alert alert-info" role="alert">
                    No dispones de ningún ticket de descuento para canjear
                </div>
            </div>
        );
    }

    return (

        <div>
            <DiscountTickets discountTickets = {discountTicketSearch.result.items} />
            <Pager 
                back={{
                    enabled: discountTicketSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindDiscountTicketsResultPage(discountTicketSearch.criteria))}}
                next={{
                    enabled: discountTicketSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindDiscountTicketsResultPage(discountTicketSearch.criteria))}}/>
        </div>

    );

}

export default FindDiscountTicketsResult;
