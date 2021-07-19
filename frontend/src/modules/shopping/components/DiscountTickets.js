import React, {useState} from 'react';
import PropTypes from 'prop-types';

import DiscountTicket from './DiscountTicket';
import {Errors} from '../../common';

const DiscountTickets = ({discountTickets}) => {

    return(

        <div>

            {discountTickets.map(discountTicket => 
                <DiscountTicket key={discountTicket.id}
                    discountTicket={discountTicket}/>
            )}

        </div>

    );
};

DiscountTickets.propTypes = {
    discountTickets: PropTypes.array.isRequired,
};

export default DiscountTickets;
