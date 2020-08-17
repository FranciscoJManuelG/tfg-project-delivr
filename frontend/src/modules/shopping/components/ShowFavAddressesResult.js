import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as usersActions from '../../users/actions';
import * as usersSelectors from '../../users/selectors';
import * as businessSelectors from '../../business/selectors'
import {Pager} from '../../common';
import FavAddresses from './FavAddresses';

const ShowFavAddressesResult = ({companyId}) => {

    const favouriteAddressSearch = useSelector(usersSelectors.getFavouriteAddressSearch);
    const cities = useSelector(businessSelectors.getCities);
    const dispatch = useDispatch();

    if (!favouriteAddressSearch) {
        return null;
    }

    if (favouriteAddressSearch.result.items.length === 0) {
        return (
            <div>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.users.FindFavouriteAddressesResult.noFavouriteAddresses'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            <FavAddresses favouriteAddresses={favouriteAddressSearch.result.items} cities={cities} companyId={companyId}
                onSelectItem={(...args) => dispatch(usersActions.findFavAddress(...args))}/>
            <Pager 
                back={{
                    enabled: favouriteAddressSearch.criteria.page >= 1,
                    onClick: () => dispatch(usersActions.previousFindFavouriteAddressesResultPage(favouriteAddressSearch.criteria))}}
                next={{
                    enabled: favouriteAddressSearch.result.existMoreItems,
                    onClick: () => dispatch(usersActions.nextFindFavouriteAddressesResultPage(favouriteAddressSearch.criteria))}}/>
        </div>

    );

}

export default ShowFavAddressesResult;
