import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import * as actions from '../actions';
import * as selectors from '../selectors';
import * as businessSelectors from '../../business/selectors'
import {Pager} from '../../common';
import FavouriteAddresses from './FavouriteAddresses';
import Sidebar from '../../common/components/UserSidebar'

const FindFavouriteAddressesResult = () => {

    const favouriteAddressSearch = useSelector(selectors.getFavouriteAddressSearch);
    const cities = useSelector(businessSelectors.getCities);
    const dispatch = useDispatch();

    if (!favouriteAddressSearch) {
        return null;
    }

    if (favouriteAddressSearch.result.items.length === 0) {
        return (
            <div>
                <Sidebar/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.users.FindFavouriteAddressesResult.noFavouriteAddresses'/>
                </div>
                <Link to='/users/add-favourite-address'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
            </div>
        );
    }

    return (

        <div>
            <Sidebar/>
            <FavouriteAddresses favouriteAddresses={favouriteAddressSearch.result.items} cities={cities}
                onDeleteItem={(...args) => dispatch(actions.deleteFavouriteAddress(...args))}/>
            <Pager 
                back={{
                    enabled: favouriteAddressSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindFavouriteAddressesResultPage(favouriteAddressSearch.criteria))}}
                next={{
                    enabled: favouriteAddressSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindFavouriteAddressesResultPage(favouriteAddressSearch.criteria))}}/>
            <Link to='/users/add-favourite-address'>
                <div className="form-group">
                    <button type="submit" className="btn btn-primary">Añadir</button>
                </div> 
            </Link>
        </div>

    );

}

export default FindFavouriteAddressesResult;
