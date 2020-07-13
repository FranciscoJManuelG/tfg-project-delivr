import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindFavouriteAddresses = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findFavouriteAddresses({page: 0}));
        history.push('/users/find-favourite-addresses-result');

    });

    return null;

}

export default FindFavouriteAddresses;
