import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as usersActions from '../../users/actions';
import {useParams} from 'react-router-dom';

const ShowFavAddresses = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const {id} = useParams();

    useEffect(() => {

        dispatch(usersActions.findFavouriteAddresses({page: 0}));
        history.push(`/shopping/set-address-to-send-purchase/${id}`);

    });

    return null;

}

export default ShowFavAddresses;
