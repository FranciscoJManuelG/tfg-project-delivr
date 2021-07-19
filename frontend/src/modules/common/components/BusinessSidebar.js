import React from 'react';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

const BusinessSidebar = () => {
    
    return (
        <div>
            <div className="sidebar-heading">Ajustes de empresa</div>
                <div className="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/business/modify-company">
                        <FormattedMessage id="project.business.ModifyCompany.title"/>
                    </Link>
                </div>
                <div className="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/business/find-company-addresses">
                        <FormattedMessage id="project.business.FindAddresses.title"/>
                    </Link>
                </div>
                <div className="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/business/state-company">
                        <FormattedMessage id="project.business.BlockCompany.title"/>
                    </Link>
                </div>
                <div className="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/business/add-goal">
                        <FormattedMessage id="project.business.AddGoal.title"/>
                    </Link>
                </div>
                <div className="list-group list-group-flush">
                    <Link className="list-group-item list-group-item-action" to="/business/find-goals">
                        <FormattedMessage id="project.business.FindGoals.title"/>
                    </Link>
                </div>
        </div>
    );

};

export default BusinessSidebar;

