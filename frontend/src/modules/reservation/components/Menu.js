import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import MenuItemList from './MenuItemList';
import * as selectors from '../selectors';
import * as actions from '../actions';

const Menu = ({companyId, reservationDate, periodType, diners}) => {

    const menu = useSelector(selectors.getMenu);
    const dispatch = useDispatch();
    const history = useHistory();

    return (

        <div>
            <MenuItemList list={menu} edit companyId = {companyId}
                onUpdateQuantity={(...args) => dispatch(actions.updateMenuItemQuantity(...args))}
                onRemoveItem={(...args) => dispatch(actions.removeMenuItem(...args))}/>

            {menu.items.length > 0 &&

                <div className="text-center">   
                    <button type="button" className="btn btn-primary"
                        onClick={() => history.push(`/reservation/reservation-details/${companyId}/${reservationDate}/${periodType}/${diners}`)}>
                        Reservar
                    </button>
                </div>
            }

        </div>

    );

}
export default Menu;
