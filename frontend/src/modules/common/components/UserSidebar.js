import React from 'react';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

const UserSidebar = () => {
    
    return (
        <div>
            <div class="sidebar-heading">Ajustes de usuario</div>
                <div class="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/users/change-password">
                        <FormattedMessage id="project.users.ChangePassword.title"/>
                    </Link>
                </div>
                <div class="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/users/update-profile">
                        <FormattedMessage id="project.users.UpdateProfile.title"/>
                    </Link>
                </div>
        </div>
    );

};

export default UserSidebar;

