import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as usersActions from '../../users/actions';
import {useParams} from 'react-router-dom';

const ShowFavAddresses = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const {id, cityId} = useParams();
    let companyCityId = cityId;

    useEffect(() => {

        dispatch(usersActions.findFavouriteAddressesByCity(cityId, {page: 0}));
        history.push(`/shopping/set-address-to-send-purchase/${id}/${companyCityId}`);

    });

    return null;

}

export default ShowFavAddresses;
